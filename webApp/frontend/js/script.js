'use strict';

$(document).ready(function () {

    $("#login-form").submit(function(event) {
        let email = document.getElementById("inputUsername").value;
        let password = document.getElementById("inputPassword").value;

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: 'http://localhost:8080/portfolio/login',
            // data: new FormData(this),
            data: '{"email":"'+email+'", "password":"'+password+'"}',
            contentType: "application/json; charset=utf8",
            cache: false,
            processData:false,
            success: function(response){
                console.log(response);
                if(email == "admin@gmail.com" && response == true) {
                    window.location.href = "./adminPage.html";
                } else {
                    if (response) {
                        window.location.href = "./userPage.html";
                    } else {
                        $("#error").html("<p>Incorrect Email Or Password. Please try again.</p>");
                        document.getElementById("inputUsername").value = '';
                        document.getElementById("inputPassword").value = '';
                    }
                }           
            },
            error: function (error) {
                console.log(error);
            }
        });
        event.preventDefault();
    });

    $("#signup-form").submit(function(event) {
        let name = document.getElementById("inputName").value;
        let contact = document.getElementById("inputContact").value;
        let email = document.getElementById("inputUsername").value;
        let password = document.getElementById("inputPassword").value;
        let confirmpassword = document.getElementById("inputConfirmPassword").value;

        let atposition=email.indexOf("@");  
        let dotposition=email.lastIndexOf(".");  

        if (atposition < 1 || dotposition < atposition+2 || dotposition + 2 >= email.length) {  
            $("#error").html("<p>Please enter a valid email address.</p>");
        } else if(password != confirmpassword) {
            $("#error").html("<p>Passwords did not match</p>");
        } else if(contact.length != 10) {
            $("#error").html("<p>Contact must be 10 digits long.</p>");
        } else {
            $.ajax({
                type: 'POST',
                enctype: 'multipart/form-data',
                url: 'http://localhost:8080/portfolio/register',
                // data: new FormData(this),
                data: '{"name":"'+name+'", "email":"'+email+'", "password":"'+password+'", "contact":"'+contact+'"}',
                contentType: "application/json; charset=utf8",
                cache: false,
                processData:false,
                success: function(response){
                    if (response) {
                        console.log(response);
                        window.location.href = "./login.html";
                    } else {
                        $("#error").html("<p>Signup Failed. Please try again.</p>");
                        console.log(response);
                        document.getElementById("inputName").value = '';
                        document.getElementById("inputContact").value = '';
                        document.getElementById("inputUsername").value = '';
                        document.getElementById("inputPassword").value = ''; 
                    }
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
        event.preventDefault();
    });

    $("#file-upload").submit(function(event) {
        var input = document.querySelector('input[type="file"]')
    
        var dataFile = new FormData()
        dataFile.append('file', input.files[0])
        // alert("file : " +dataFile);
        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: 'http://localhost:8080/api/upload',
            // data: new FormData(this),
            data: dataFile,
            contentType: false,
            cache: false,
            processData:false,
            success: function(response){
                console.log("success");
                $("#upload-status").html("<p>File Uploaded Successfully.</p>");
            },
            error: function (error) {
                console.log(error);
            }
        });
        event.preventDefault();
    });

    $("#view").submit(function(event) {
        let table = document.getElementById('table');
        document.getElementById('tp').value = '';
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/view',
            contentType: false,
            cache: false,
            processData:false,
            data:'',
            success: function(response){
                console.log("success");
                // let my_content = '';
                // let i = 0;
                // for (i = 0; i < response.length; ++i) {
                //     my_content += 'ID : ' + response[i].id;
                //     my_content += '<br>File-Name : ' + response[i].name;
                //     my_content += '<br>File-Content : ' + response[i].content + '<br><br>';
                // }
                // $("#view").html("<p>"+my_content+"</p>");
                
                $("#table tr").remove(); 
                let tr1 = document.createElement('tr');

                let th1 = document.createElement('th');
                th1.textContent = "ID";
                tr1.appendChild(th1);

                let th2 = document.createElement('th');
                th2.textContent = "FILE NAME";
                tr1.appendChild(th2);

                let th3 = document.createElement('th');
                th3.textContent = "FILE CONTENT";
                tr1.appendChild(th3);

                table.appendChild(tr1);
                let i = 0;
                for (let res of response) {
                    i++;
                    let tr = document.createElement('tr');
                    
                    let td1 = document.createElement('td');
                    td1.textContent = res.id;
                    tr.appendChild(td1);
                    
                    let td2 = document.createElement('td');
                    td2.textContent = res.name;
                    tr.appendChild(td2);
                    
                    let td3 = document.createElement('td');
                    td3.textContent = res.content;
                    tr.appendChild(td3);

                    let td4 = document.createElement('td');
                    var button = document.createElement('button');   
                    button.id = "del";
                    button.value = res.id;  
                    // button.onclick = del();    
                    var text = document.createTextNode("Delete");
                    button.appendChild(text);
                    button.addEventListener("click", () => dele(res.id));
                    td4.appendChild(button); 
                    tr.appendChild(td4);
                    
                    table.appendChild(tr);
                }
            },
            error: function (error) {
                console.log(error);
            }
        });
        event.preventDefault();
    });

    $("#downloadfile").submit(function(event) {
        let filename = document.getElementById("str").value;
        let output_text = '';
        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: 'http://localhost:8080/api/download',
            data: filename,
            contentType: false,
            cache: false,
            processData:false,
            success: function(response){
                console.log("success");
                let i = 0;
                output_text = '';
                for (i = 0; i < response.length; ++i) {
                    if (response.charAt(i) == '\n') {
                        output_text += '<br/>';
                    }
                    else {
                        output_text += response.charAt(i);
                    }
                };
                
                // $("#uploadresult").html("<li><p>"+output_text+"</p><span></span></li>");

                let newContent = response.replaceAll("<br />","\n");
                newContent = newContent.replaceAll("\n√\n"," √");
                var element = document.createElement('a');
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(newContent));
                element.setAttribute('download', "download.txt");
                element.style.display = 'none';
                document.body.appendChild(element);
                element.click();
                document.body.removeChild(element);
                $("#div-status").html("<p>File Downloaded Successfully.</p>");
                document.getElementById("str").value = '';
            },
            error: function (error) {
                let output_text = "File Not Found.";
                $("#div-status").html("<p>"+output_text+"</p>");
                console.log(error);
            }
        });
        event.preventDefault();
    });
});

function dele(id) {
    // alert(id);
    $.ajax({
        type: 'POST',
        enctype: 'multipart/form-data',
        url: 'http://localhost:8080/api/delete',
        // data: new FormData(this),
        data: String(id),
        contentType: false,
        cache: false,
        processData:false,
        success: function(response){
            console.log("success");
            alert("File is Deleted successfully.");
            self.location['reload']();
        },
        error: function (error) {
            console.log(error);
        }
    });
}
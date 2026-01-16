           // Show and hide sign up button function
            function hidenSignUp() {
                const check = document.getElementById("gridCheck");
                if (check.checked === true) {
                    document.getElementById("signup").style.visibility = "visible";
                } else {
                    document.getElementById("signup").style.visibility = "hidden";
                }
            }

            // Confirm password function
            function confirmPass() {
                const pass = document.getElementById("password").value;
                const cpass = document.getElementById("cpassword").value;

                if (pass !== cpass) {
                    document.getElementById("confirm").innerHTML = "Wrong. Please enter again!";
                    return false;
                } else {
                    document.getElementById("confirm").innerHTML = "";
                    return true;
                }
            }

            // Validate phone number function
            function validatePhone() {
                const phone = document.getElementById("phoneNumber").value;
                const phonePattern = /^\d{10}$/;
                if (!phonePattern.test(phone)) {
                    document.getElementById("phoneError").innerHTML = "Invalid phone number. It should be 10 digits.";
                    return false;
                } else {
                    document.getElementById("phoneError").innerHTML = "";
                    return true;
                }
            }

            // Validate password function
            function validatePass() {
                const password = document.getElementById("password").value;
                if (password.length < 6) {
                    document.getElementById("passwordError").innerHTML = "Password must be at least 6 characters long.";
                    return false;
                }
                document.getElementById("passwordError").innerHTML = "";
                return true;
            }

            // Validate email function
            function validateEmail() {
                const email = document.getElementById("email").value;
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailPattern.test(email)) {
                    document.getElementById("emailError").innerHTML = "Invalid email format.";
                    return false;
                }
                document.getElementById("emailError").innerHTML = "";
                return true;
            }

            // Validate fullname function
            function validateName() {
                const fullname = document.getElementById("fullname").value;
                if (fullname.trim() === "") {
                    document.getElementById("nameError").innerHTML = "Fullname is required.";
                    return false;
                }
                document.getElementById("nameError").innerHTML = "";
                return true;
            }
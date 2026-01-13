function hidenSignUp() {
  check = document.getElementById("gridCheck");
  if (check.checked == true) {
    document.getElementById("signup").style.visibility = "visible";
  } else {
    document.getElementById("signup").style.visibility = "hidden";
  }
}

function confirmPass() {
  pass = document.getElementById("password").value;
  cpass = document.getElementById("cpassword").value;

  if (pass != cpass) {
    document.getElementById("confirm").innerHTML =
      " Wrong. Please enter again!";
    return false;
  } else {
    document.getElementById("confirm").innerHTML = "";
    return true;
  }
}

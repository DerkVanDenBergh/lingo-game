function validate() {
    if (document.login.username.value == "" && document.login.password.value == "") {
        alert("${noUser} and ${noPass}");
        document.login.username.focus();
        return false;
    }
    if (document.login.username.value == "") {
        alert("${noUser}");
        document.login.username.focus();
        return false;
    }
    if (document.login.password.value == "") {
        alert("${noPass}");
        document.login.password.focus();
        return false;
    }
}
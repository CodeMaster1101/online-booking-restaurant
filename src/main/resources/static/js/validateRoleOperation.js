function formValidationAdmin(){
	let radios = document.getElementsByName("role");
    let formValid = false;

    let i = 0;
    while (!formValid && i < radios.length) {
        if (radios[i].checked) formValid = true;
        i++;        
    }
    if (!formValid) {
	alert("Please select a role!");
	return false;
	}
	return true;
}
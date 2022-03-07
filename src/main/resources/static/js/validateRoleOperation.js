function formValidationAdmin(){
	let radios = document.getElementsByName("type");
    let formValid = false;

    let i = 0;
    while (!formValid && i < radios.length) {
        if (radios[i].checked) formValid = true;
        i++;        
    }
    if (!formValid) {
	setError(document.getElementById('roleError'), "Please select a role!");
	return false;
	}
	return true;
}
const setError = (element, message) => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
}
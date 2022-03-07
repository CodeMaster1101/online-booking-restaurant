const setError = (element, message) => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
}

function calcDate(date1){

	const dt_date1 = new Date(date1);
	const dt_date2= new Date();

	//Get the Timestamp
	date1 = dt_date1.getTime();
	let date2 = dt_date2.getTime();
	
	let calc;
	//Check which timestamp is greater
	if (date1 > date2){
    calc = new Date(date1 - date2) ;
	}
	else{
    return false;
	}
	
	let daysPassed = parseInt(Math.abs(calc.getDate()))
	if(!daysPassed >= 1){
		return false
	}
	return true;
	
}

function formValidation() {
	
	//accepted validation
	let checkbox = document.getElementById('accept');
	if(!checkbox.checked){
		setError(checkbox, "Terms and conditions must be accepted!")
		return false;
	}
	//date validation
	
	let date = document.querySelector('input[type="date"]');
	if(date.value === ""){
		setError(date, "Please specify a date");
		return false;
	}
	if(!calcDate(date.value)){
		setError(date, "the minimal time for reserving must be a day from now. Try again.");
		return false;
	}
	//period of day validation
	let radios = document.getElementsByName("period");
    let formValid = false;
	
    let i = 0;
    while (!formValid && i < radios.length) {
        if (radios[i].checked) formValid = true;
        i++;        
    }
    if (!formValid) {
	setError(document.getElementById('periodError'), "Please select a period!")
	return false;
	}
	//time validation
	i = i - 1;
	let fetchedTime = document.getElementById('specTime');
	if(fetchedTime.value === ""){
		setError(fetchedTime, "Specify a time!");		
		return false;
	}
	let time = new Date();
	time.setTime(fetchedTime.valueAsNumber)
	let CONSTANTS = {"START": new Date("January 01 1970 08:00"), 
					"NOON": new Date("January 01 1970 13:00"),
					"EVENING": new Date("January 01 1970 21:00"), 
					"END": new Date("January 01 1970 23:00")};
	if(i === 0) {
			if(time > CONSTANTS.START && 
					time <= CONSTANTS.NOON) return true;
		}
		else if(i === 1) {
			if(time >= CONSTANTS.NOON && 
					time < CONSTANTS.EVENING) return true;
		}
		else if(i === 2) {
			if(time >= CONSTANTS.EVENING && 
				 time <= CONSTANTS.END) return true;
		}
	setError(fetchedTime, "Specify a time within the period you have selected!");		
	return false;
}
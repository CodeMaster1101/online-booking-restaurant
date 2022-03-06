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
		alert("Terms and conditions must be acepted")
		return false;
	}
	//date validation
	
	let date = document.querySelector('input[type="date"]');
	if(date.value === ""){
		alert("Please specify a date");
		return false;
	}
	if(!calcDate(date.value)){
		alert("the minimal time for reserving must be a day from now. Try again.")
		return false;
	}
	//period od day validation
	let radios = document.getElementsByName("period");
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
	//time validation
	/*
	
	let time = document.getElementById('specTime');
	let CONSTANTS = {"START": new Time("6:59"), "NOON": new Time("12:00"), 
					"EVENING": new Time("20:00"), "END": new Time("22:00")};
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
				 time <= CONSTANTS.END )return true;
		}		
	return false;
	*/
	return true;
}
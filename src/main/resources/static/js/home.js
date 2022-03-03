function breakfast(){
	let period = document.getElementById("breakfast");
	period.setAttribute('value', 1);
}
function lunch(){
	let period = document.getElementById("lunch");
	period.setAttribute('value', 2);
}
function dinner(){
	let period = document.getElementById("dinner");
	period.setAttribute('value', 3);
}

function formValidation() {
	
	let accepted = document.forms["reserve"]["accepted"].value;
	let date = (document.forms["reserve"]["date"].value);
	let p1 = document.forms["reserve"]["p1"].value;
	let p2 = document.forms["reserve"]["p2"].value;
	let p3 = document.forms["reserve"]["p3"].value;
	const periods = {p1, p2, p3};
	let time = document.forms["reserve"]["time"].value;
	
	if(accepted === false || accepted === null){
		alert("Terms and conditions must be accepted");
		return false;
	}
	let differenceInDates = date.diff( new Date(), 'days');
	if(date === null || differenceInDates < 1){
		alert("Invalid date. The minimum time for the reservation must be a day from now. For the time of your reservation, which is: "
				+ date + " The minimal time would be " + date.setDate(date.getDate() + 1));
		return false
	}
	let x = false;
	for(let i = 0; i < periods.length; i++){
		if(periods[i] !== 0){
			x = true;
		}
	}
	if(x === false){
		alert("Please specify a period.")
		return false;	
	}
	if(time === null){
		alert("Specify a time!")
		return false;
	}
	
	return true;
}

function formValidationAdmin(){
	let p1 = document.forms["reserve"]["p1"].value;
	let p2 = document.forms["reserve"]["p2"].value;
	let p3 = document.forms["reserve"]["p3"].value;
	const periods = {p1, p2, p3};
	let x = false;
	for(let i = 0; i < periods.length; i++){
		if(periods[i] !== 0){
			x = true;
		}
	}
	if(x === false){
		alert("Please specify a role.")
		return false;	
	}
}

const menu = document.querySelector('#mobile-menu');
const menuLinks = document.querySelector('.navbar__menu');
const navLogo = document.querySelector('#navbar__logo');

// Display Mobile Menu
const mobileMenu = () => {
  menu.classList.toggle('is-active');
  menuLinks.classList.toggle('active');
};

menu.addEventListener('click', mobileMenu);

// Show active menu when scrolling
const highlightMenu = () => {
  const elem = document.querySelector('.highlight');
  const homeMenu = document.querySelector('#home-page');
  const aboutMenu = document.querySelector('#about-page');
  const servicesMenu = document.querySelector('#services-page');
  let scrollPos = window.scrollY;

  // adds 'highlight' class to my menu items
  if (window.innerWidth > 960 && scrollPos < 600) {
    homeMenu.classList.add('highlight');
    aboutMenu.classList.remove('highlight');
    return;
  } else if (window.innerWidth > 960 && scrollPos < 1400) {
    aboutMenu.classList.add('highlight');
    homeMenu.classList.remove('highlight');
    servicesMenu.classList.remove('highlight');
    return;
  } else if (window.innerWidth > 960 && scrollPos < 2345) {
    servicesMenu.classList.add('highlight');
    aboutMenu.classList.remove('highlight');
    return;
  }

  if ((elem && window.innerWIdth < 960 && scrollPos < 600) || elem) {
    elem.classList.remove('highlight');
  }
};

window.addEventListener('scroll', highlightMenu);
window.addEventListener('click', highlightMenu);

//  Close mobile Menu when clicking on a menu item
const hideMobileMenu = () => {
  const menuBars = document.querySelector('.is-active');
  if (window.innerWidth <= 768 && menuBars) {
    menu.classList.toggle('is-active');
    menuLinks.classList.remove('active');
  }
};

menuLinks.addEventListener('click', hideMobileMenu);
navLogo.addEventListener('click', hideMobileMenu);
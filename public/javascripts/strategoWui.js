let dropdown = document.getElementsByClassName("dropdown");

function setOpenClass() {
    this.classList.toggle("open");
}
Array.from(dropdown).forEach(e => e.addEventListener('click', setOpenClass));


let passable = document.getElementsByClassName("passable");

function setSelectedClass() {
    this.getElementsByClassName("figure")[0].classList.add("selected");
}

Array.from(passable).forEach(e => e.addEventListener('mouseover', setSelectedClass));

function removeSelectedClass() {
    this.getElementsByClassName("figure")[0].classList.remove("selected");
}

Array.from(passable).forEach(e => e.addEventListener('mouseout', removeSelectedClass));
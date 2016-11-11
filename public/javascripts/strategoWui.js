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


$(function() {
    reload();

    $(".selectCell").click(function(event){
        $.ajax({
            type: "POST",
            url: '/strategoWui/add',
            contentType: "text/json",
            data: JSON.stringify(
                {
                    'row': 2,
                    'column': 1,
                    'rank': 5,
                }),
            success: function(responseTxt) {
                refresh(JSON.parse(responseTxt));
            },
            error:  function() {
                alert("Post Error");
            },
        });

    });
});

function reload() {
    $.ajax({
        type: "GET",
        url: '/strategoWui/refresh',
        success: function(responseTxt) {
            refresh(JSON.parse(responseTxt));
        },
        error:  function() {
            alert("Get Error");
        },
    });
}

function refresh(strategoJson) {
    // TODO refreshSelect und refreshInfo
    refreshField(strategoJson.field, strategoJson.playerOne, strategoJson.playerTwo);
}

function refreshField(field, playerOne, playerTwo) {
    field.innerField.forEach(item => {
        let cell = $(".cellBorder[data-row='" + item.row + "'][data-column='" + item.column+ "'] .figure")
        if (item.containsCharacter) {
            let character = item.character;
            if(character.player === playerOne) {
                cell.addClass("player1");
                cell.removeClass("player2");
            }
            if(character.player === playerTwo) {
                cell.addClass("player2");
                cell.removeClass("player1");
            }
            if (character.isVisible) {
                cell.attr('src', 'assets/images/figures/'+ character.rank +'.svg');
            } else {
                cell.attr('src', 'assets/images/figures/empty.svg');
            }
        } else {
            cell.removeClass("player1");
            cell.removeClass("player2");
            cell.attr('src', 'assets/images/figures/empty.svg');
        }
    });
}
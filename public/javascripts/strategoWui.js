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

let addEventCharacterRank;

$(function() {
    reload();

    $(".selectCell .figure").click(function(event) {
        addEventCharacterRank = $(this).attr("data-rank");
    });
    
    $(".passable").click(function(event) {
        if (addEventCharacterRank !== undefined) {
            $.ajax({
                type: "POST",
                url: '/strategoWui/add',
                contentType: "text/json",
                data: JSON.stringify(
                    {
                        'row': parseInt($(this).attr("data-row")),
                        'column': parseInt($(this).attr("data-column")),
                        'rank': parseInt(addEventCharacterRank)
                    }),
                success: function(responseTxt) {
                    refresh(JSON.parse(responseTxt));
                },
                error:  function() {
                    alert("Post Error");
                },
            });
            addEventCharacterRank = undefined;
        }
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
    // TODO refreshInfo
    refreshSelect(strategoJson.select, strategoJson.playerOne, strategoJson.playerTwo);
    refreshField(strategoJson.field, strategoJson.playerOne, strategoJson.playerTwo);
}

function refreshSelect(select, playerOne, playerTwo) {
    for (i = 0; i < 40; i++) {
        let selectCell = $($(".selectCell .figure")[i]);
        let character = select.characterList[i];
        if (i < select.characterList.length) {
            if(character.player === playerOne) {
                selectCell.addClass("player1");
                selectCell.removeClass("player2");
            }
            if(character.player === playerTwo) {
                selectCell.addClass("player2");
                selectCell.removeClass("player1");
            }
            selectCell.attr('src', 'assets/images/figures/' + character.rank + '.svg');
            selectCell.attr('data-rank', character.rank);
        } else {
            selectCell.removeClass("player1");
            selectCell.removeClass("player2");
            selectCell.attr('src', 'assets/images/figures/empty.svg');
            selectCell.removeAttr("data-rank");
        }
    }
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
                cell.attr('src', 'assets/images/figures/' + character.rank + '.svg');
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
dropdownNavbar();

let addEventCharacterRank;
let lastClickedCell = "nothingSelected";

$(function() {
    reload();
    addRemoveHoverOnPassableCells();
    addRemoveHoverOnSelectCell();
    clickActionsOnFigure();
});

function clickActionsOnFigure() {
    let leftMouseButton = 1;

    $(".figure").mouseup(
        function(event) {
            if (event.which === leftMouseButton) {
                $(this).css("filter", "");
                this.classList.toggle("selected");
                whichInputIsMade($(this).parent(), $(this));
            }
        }
    ).mousedown(
        function(event) {
            if (event.which === leftMouseButton) {
                $(this).css("filter", "brightness(3%)");
            }
        }
    ).mouseleave( // to remove even when you click and drag
        function () {
            $(this).css("filter", "");
        }
    );
}

function whichInputIsMade(parentCell, figure) {
    if (parentCell.hasClass("selectCell")) {
        if (lastClickedCell === "selectCell") {
            lastClickedCell = "nothingSelected";
            removeSelected();
        } else if (lastClickedCell === "nothingSelected") {
            lastClickedCell = "selectCell";
            addEventCharacterRank = figure.attr("data-rank");
            console.log(addEventCharacterRank);
        } else if (lastClickedCell === "cellBorder") {
            // TODO Input r remove
            lastClickedCell = "nothingSelected";
            removeSelected();
        }
    }
    if (parentCell.hasClass("cellBorder")) {
        if (lastClickedCell === "selectCell") {
            lastClickedCell = "nothingSelected";
            add(parentCell);
            removeSelected();
        } else if (lastClickedCell === "nothingSelected") {
            lastClickedCell = "cellBorder";
           // TODO Input r remove save Rank
        } else if (lastClickedCell === "cellBorder") {
            // TODO Input m move when in play mode
            lastClickedCell = "nothingSelected";
            removeSelected();
        }
    }
    console.log(lastClickedCell);
}

function removeSelected() {
    $(".selected").removeClass("selected");
}

function add(parentCell) {
    if (addEventCharacterRank !== undefined) {
        $.ajax({
            type: "POST",
            url: '/strategoWui/add',
            contentType: "text/json",
            data: JSON.stringify(
                {
                    'row': parseInt(parentCell.attr("data-row")),
                    'column': parseInt(parentCell.attr("data-column")),
                    'rank': parseInt(addEventCharacterRank)
                }),
            success: function (responseTxt) {
                refresh(JSON.parse(responseTxt));
            },
            error: function () {
                alert("Post Error");
            },
        });
        addEventCharacterRank = undefined;
    }
}

function addRemoveHoverOnSelectCell() {
    function enterCell() {
        this.classList.add("hover");
    }

    function leaveCell() {
        this.classList.remove("hover");
    }

    $(".selectCell .figure").hover(enterCell, leaveCell)
}

function addRemoveHoverOnPassableCells() {
    let passable = document.getElementsByClassName("passable");

    function setSelectedClass() {
        this.getElementsByClassName("figure")[0].classList.add("hover");
    }

    Array.from(passable).forEach(e => e.addEventListener('mouseover', setSelectedClass));

    function removeSelectedClass() {
        this.getElementsByClassName("figure")[0].classList.remove("hover");
    }

    Array.from(passable).forEach(e => e.addEventListener('mouseout', removeSelectedClass));
}

function dropdownNavbar() {
    let dropdown = document.getElementsByClassName("dropdown");

    function setOpenClass() {
        this.classList.toggle("open");
    }
    Array.from(dropdown).forEach(e => e.addEventListener('click', setOpenClass));
}

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

    let {select: select, field: field, playerOne: playerOne, playerTwo: playerTwo} = strategoJson;

    refreshSelect(select, playerOne, playerTwo);
    refreshField(field, playerOne, playerTwo);
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
                cell.attr('src', 'assets/images/figures/notVisible.svg');
            }
        } else {
            cell.removeClass("player1");
            cell.removeClass("player2");
            cell.attr('src', 'assets/images/figures/empty.svg');
        }
    });
}
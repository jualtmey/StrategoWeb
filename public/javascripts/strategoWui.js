dropdownNavbar();

let addEventCharacterRank;
let lastClickedCell;

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
        if (lastClickedCell === undefined) {
            lastClickedCell = parentCell;
            addEventCharacterRank = figure.attr("data-rank");
            console.log(addEventCharacterRank);
        } else if (lastClickedCell.hasClass("selectCell")) {
            lastClickedCell = undefined;
            removeSelected();
        } else if (lastClickedCell.hasClass("cellBorder")) {
            // TODO Input r remove
            lastClickedCell = undefined;
            removeSelected();
        }
    }
    if (parentCell.hasClass("cellBorder")) {
        if (lastClickedCell === undefined) {
            lastClickedCell = parentCell;
           // TODO Input r remove save Rank
        } else if (lastClickedCell.hasClass("selectCell")) {
            lastClickedCell = undefined;
            add(parentCell);
            removeSelected();
        } else if (lastClickedCell.hasClass("cellBorder")) {
            // TODO Input m move when in play mode
            swap(parentCell);
            lastClickedCell = undefined;
            removeSelected();
        }
    }
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

function swap(parentCell) {
    $.ajax({
        type: "POST",
        url: '/strategoWui/swap',
        contentType: "text/json",
        data: JSON.stringify(
            {
                'row1': parseInt(lastClickedCell.attr("data-row")),
                'column1': parseInt(lastClickedCell.attr("data-column")),
                'row2': parseInt(parentCell.attr("data-row")),
                'column2': parseInt(parentCell.attr("data-column")),
            }),
        success: function (responseTxt) {
            refresh(JSON.parse(responseTxt));
        },
        error: function () {
            alert("Post Error");
        },
    });
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
    let {select: select, field: field, info: info, playerOne: playerOne, playerTwo: playerTwo} = strategoJson;

    refreshSelect(select, playerOne, playerTwo);
    refreshField(field, playerOne, playerTwo);
    refreshInfo(info, playerOne, playerTwo);
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

function refreshInfo(info, playerOne, playerTwo) {
    let infoTableHead = $("#infoTable thead th");
    let infoTableBody = $("#infoTable tbody tr");
    let infoList = info.infoList;

    $($(infoTableHead[1]).html(playerOne));
    $($(infoTableHead[2]).html(playerTwo));
    
    for (rank = 0; rank < infoList.length; rank++) {
        $($(infoTableBody[rank]).find("th")[1]).html(infoList[rank].currentCharactersPlayerOne + "/" + infoList[rank].maxCharacters);
        $($(infoTableBody[rank]).find("th")[2]).html(infoList[rank].currentCharactersPlayerTwo + "/" + infoList[rank].maxCharacters);
    }
}



















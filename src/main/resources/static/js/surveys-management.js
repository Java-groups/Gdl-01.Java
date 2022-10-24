"use strict";

const openClosed = document.querySelector('#openClosedContainer');
const newSurvey = document.querySelector('.new-survey');
const newSurveyContainer = document.querySelector('.new-survey-container');

let counter = 1;

loadEventListeners();

function loadEventListeners() {
    openClosed.addEventListener('click', switchSurveys);
    newSurvey.addEventListener('click', createNewSurvey);
    newSurveyContainer.addEventListener('click', newSurveyContainerManager);
}

function padTo2Digits(num) {
    return num.toString().padStart(2, '0');
}

function createDate(){
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();

    let hour = padTo2Digits(date.getHours());
    let minutes = padTo2Digits(date.getMinutes());
    let seconds = padTo2Digits(date.getSeconds());

    return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
}

function createNewSurvey() {
    clearPollInfoContainer();
    let newPollForm = document.createElement('form');

    newPollForm.innerHTML = `
        <div class="form-group">
            <label id="question" for="questionArea">Question</label>
            <textarea class="form-control" id="questionText" rows="3"> </textarea>
        </div>

        <div class="input-group is-invalid options-container">
            <div class="option">
                <div class="custom-file">
                    <input type="text" id="optionText${counter}" required> </input>
                    <button class="btn btn-outline-secondary" type="button" id="delete-element"> X </button>
                </div>
            </div>
        </div>

        <button type="button" class="btn btn-outline-danger add-option">+Add</button>
        <div>
            <button type="button" class="btn btn-outline-danger cancel">Cancel</button>
            <button type="button" class="btn btn-danger save">Save</button>        
        </div>
    `;

    newSurvey.disabled = true;
    newSurveyContainer.appendChild(newPollForm);
}

function newSurveyContainerManager(e) {
    if (e.target.classList.contains("add-option")) {
        addAnswerOption();
    } else if (e.target.classList.contains("save")) {
        save();
    } else if (e.target.classList.contains("cancel")) {
        cancel();
    }
}

function postSurvey(survey) {
    fetch('api/surveys', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(survey)
    });
}

function save() {
    let survey = {
        "poll": {
            "question": "",
            "userId": 1,
            "pollOptions": [ ]
        }
    };

    let question = document.querySelector('#questionText').value;
    let newOption = { };

    survey.poll.creationDate = createDate();
    survey.poll.closedDate = createDate();
    survey.poll.modificationDate = createDate();
    survey.poll.question = question;

    for (let i = 1; i <= counter; i++) {
        newOption = {
            "value": document.querySelector(`#optionText${i}`).value,
            "order": i
        };
        survey.poll.pollOptions.push(newOption);
    }

    postSurvey(survey);
    cancel();
}

function cancel() {
    counter = 1;
    newSurvey.disabled = false;
    newSurveyContainer.innerHTML = " ";
}

function addAnswerOption(e) {
    counter++;
    let optionContainer = document.querySelector('.options-container');
    let newOption = document.createElement('div');

    newOption.setAttribute('class', 'option');
    newOption.innerHTML = `
        <div class="custom-file">
            <input type="text" id="optionText${counter}"> </input>
            <button class="btn btn-outline-secondary" type="button" id="delete-element"> X </button>
        </div>
    `;

    optionContainer.appendChild(newOption);
}

function clearPollInfoContainer() {
    let pollInfoContainer = document.querySelector('#poll-info-container');

    while (pollInfoContainer.hasChildNodes()) {
        if (pollInfoContainer.firstElementChild.getAttribute('class') !== 'new-survey-container') {
            pollInfoContainer.removeChild(pollInfoContainer.firstElementChild);
        } else {
            break;
        }
    }
}

function switchSurveys(e){
    if (e.target.classList.contains('openedSurveyBtn')){
        showOpenedSurveys();
    } else if (e.target.classList.contains('closedSurveyBtn')){
        showClosedSurveys();
    }
}

function showOpenedSurveys() {
    cleanHTML();

    let tableHeader = document.querySelector('.card-body table thead');
    let trow = document.createElement('tr');

    trow.innerHTML = `
        <th class="font-weight-bold text-gray-800">Question</th>
        <th class="font-weight-bold text-gray-800">Status</th>
        <th class="font-weight-bold text-gray-800">Responses</th>
    `;

    tableHeader.appendChild(trow);
}

function showClosedSurveys() {
    cleanHTML();

    let tableHeader = document.querySelector('.card-body table thead');
    let trow = document.createElement('tr');

    trow.innerHTML = `
        <th class="font-weight-bold text-gray-800">Question</th>
        <th class="font-weight-bold text-gray-800">Closed Date</th>
        <th class="font-weight-bold text-gray-800">Responses</th>
    `;

    tableHeader.appendChild(trow);
}

function cleanHTML(){
    let theader = document.querySelector('.card-body table thead');
    while (theader.firstChild){
        theader.removeChild(theader.firstChild);
    }
}

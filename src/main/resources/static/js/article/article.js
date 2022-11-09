/**
 * 
 */
tinymce.init({
	selector: 'textarea#default'
});

const btnCancel = document.querySelector("#btn-cancel");
btnCancel.addEventListener("click", customAlert);


function customAlert(){
	swal({
		title: "you are about to discard the changes!",
		text: "Entered information will be missed! \n Are you sure?",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				 window.location.replace('/homeArticle');
			}
		});
}
function readURL(input) {
	if (input.files && input.files[0]) {

		var reader = new FileReader();

		reader.onload = function(e) {
			$('.image-upload-wrap').hide();

			$('.file-upload-image').attr('src', e.target.result);
			$('.file-upload-content').show();

			$('.image-title').html(input.files[0].name);
		};

		reader.readAsDataURL(input.files[0]);

	} else {
		removeUpload();
	}
}

function removeUpload() {
	$('.file-upload-input').replaceWith($('.file-upload-input').clone());
	$('.file-upload-content').hide();
	$('.image-upload-wrap').show();
}
$('.image-upload-wrap').bind('dragover', function() {
	$('.image-upload-wrap').addClass('image-dropping');
});
$('.image-upload-wrap').bind('dragleave', function() {
	$('.image-upload-wrap').removeClass('image-dropping');
});

function saveArticle(){
    const form = document.querySelector("#article-form");
    const bearerCookie = localStorage.getItem('bearer');

    let bodyFormData = new FormData();
    bodyFormData.append('articleImage', form[3].files[0], form[3].files[0].name);
    bodyFormData.append('subCategory', form[5].value);
    bodyFormData.append('team', form[6].value);
    bodyFormData.append('location', form[7].value);
    bodyFormData.append('headLine', form[9].value);
    bodyFormData.append('caption', form[10].value);
    bodyFormData.append('articleDescriptionHtml', form[11].value);

    axios.post("/api/article/new", bodyFormData, {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": `Bearer ${bearerCookie}`
            },
          })
    .then((res) => {
        const generalMessage = document.querySelector("#general-message");
        generalMessage.innerHTML = res.data.message;
    })
    .catch((err) => {
        const message = err.response.data.message;
        const divAlert = document.querySelector('#error-message');
        divAlert.innerHTML = message;
    });
}

function loadComboBox(){

    const bearerCookie = localStorage.getItem('bearer');
    axios.get("/api/article/load",{
            headers: {
              "Authorization": `Bearer ${bearerCookie}`
            },
          })
    .then((res) => {
        const data = res.data;

        const categories = data.categories;
        const categoriesId = 'input-subcategory';

        populateCombo(categoriesId, categories);

        const teams = data.teams;
        const teamsId = 'input-team'

        populateCombo(teams, teams);

        const locations = data.locations;
        const locationsId = 'input-location';

        populateCombo(locationsId, locations);

    })
    .catch((err) => {
        const message = err.response.data.message;
        const divAlert = document.querySelector('#error-message');
        divAlert.innerHTML = message;
    });
}

function populateCombo(comboId, values){
    let dropdown = document.createElement("select");
    for(var i=0;i<values.length;i++){
        var opt = document.createElement("option");
        opt.text = values[i].description;
        opt.value = values[i].id;
        dropdown.options.add(opt);
    }

    let container=document.querySelector(`#${comboId}`);
    container.appendChild(dropdown);
}

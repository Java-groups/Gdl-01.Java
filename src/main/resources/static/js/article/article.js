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

function validateRequiredInputs() {
    const inputs = [{id:'input-alt', msg: 'ALT is required'}, {id:'input-article-head-line', msg: 'ARTICLE HEADLINE is required'}, {id:'input-caption', msg: 'CAPTION is required'}];

    inputs.forEach(input =>{
        const current = document.querySelector(`#${input.id}`);
        if(current.value ==='')
            throw new Error(input.msg);
    })
}

function saveArticle(){
    const form = document.querySelector("#article-form");
    const spinner = document.querySelector("#spinner-loading");
    try {
        validateRequiredInputs();

        spinner.style.display = "block";

        const bearerCookie = localStorage.getItem('bearer');

        let bodyFormData = new FormData();
        bodyFormData.append('articleImage', form[3].files[0], form[3].files[0].name);
        bodyFormData.append('subCategory', form[5].value);
        bodyFormData.append('team', form[6].value);
        bodyFormData.append('location', form[7].value);
        bodyFormData.append('headLine', form[9].value);
        bodyFormData.append('caption', form[10].value);
        bodyFormData.append('articleDescriptionHtml', tinymce.get("default").getContent());
        bodyFormData.append('articleDescription', tinymce.get("default").getContent({format: 'text'}));

        axios.post("/api/article/new", bodyFormData, {
            headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": `Bearer ${bearerCookie}`
            },
        })
            .then((res) => {
                const divAlert = document.querySelector('#error-message');
                divAlert.innerHTML = "";

                const generalMessage = document.querySelector("#general-message");
                generalMessage.innerHTML = res.data.message;
            })
            .catch((err) => {
                const message = err.response.data.message;
                const divAlert = document.querySelector('#error-message');
                divAlert.innerHTML = message;
            });
    }catch (e) {
        const generalMessage = document.querySelector("#general-message");
        generalMessage.innerHTML = "";

        const divAlert = document.querySelector('#error-message');
        divAlert.innerHTML = e.message;
    }

    spinner.style.display = "none";
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

        populateCombo(teamsId, teams);

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

    let container=document.querySelector(`#${comboId}`);

    let opt = document.createElement("option");
    opt.text = values[0].description;
    opt.value = values[0].id;
    opt.selected=true;
    container.appendChild(opt);

    for(let i=0; i<values.length; i++){
        opt = document.createElement("option");
        opt.text = values[i].description;
        opt.value = values[i].id;
        container.appendChild(opt);
    }

}

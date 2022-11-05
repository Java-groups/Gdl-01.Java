/**
 * 
 */

tinymce.init({
	selector: 'textarea#default'
});

const btnCancel = document.querySelector("#btn-cancel");
btnCancel.addEventListener("click", customAlert);

/*const btnSave = document.querySelector("#btn-save");
btnSave.addEventListener("click", submitForm);

function submitForm(){
	//const articleForm = document.querySelector("#article-form");
	document.forms["article-form"].submit();

}*/
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
    console.log(res);
    })
    .catch((err) => {
    console.log(err);
    });
}

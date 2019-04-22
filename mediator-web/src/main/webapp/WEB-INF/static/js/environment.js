/*$(document).ready(function(){
	$(window.parent.document).find("#contentFrame").load(function(){
		var main = $(window.parent.document).find("#contentFrame");
		var thisheight = $(document.body).height();
		//console.log("thisheight",thisheight);
		main.height(thisheight);
	});
});*/

//这个效果不好,计算并不准确
$(document).ready(function(){
	$("#contentFrame").load(function(){ 
		var main = $(this);
		var thisheight = $(document.getElementById('contentFrame').contentWindow.document.body).height();
		console.log("thisheight",thisheight);
		main.height(thisheight);
	}); 
});
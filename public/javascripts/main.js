$(function() {
	$('#company').chainSelect('#user', '/ajax/user', { default: '@userId' });
});

$(function() {
	$('#company').change();
});

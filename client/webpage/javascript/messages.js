function showSuccess(msg) {
  $("#error").hide();
  $("#success").html("<p>" + msg + "</p>");
  $("#success").show();
}
function showError(msg) {
  $("#success").hide();
  $("#error").html("<p>" + msg + "</p>");
  $("#error").show();
}
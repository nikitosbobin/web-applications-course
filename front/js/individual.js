$(document).ready(function() { 
    $("#get_individual_result").click(performClick);
    $("#clear_button").click(function() {
        var outDiv = $("#out_div");
        outDiv.addClass("invisible");
        outDiv.removeClass("visible");
        $("#individual_output").val("");
        $("#individual_input").val("");
    });
    $(document).keypress(function(e) {
        if(e.which == 13) {
            performClick();
        }
    });
});

function performClick() {
    var value = $("#individual_input").val();
    if (value == "") { 
        return;
    }
    $.post("http://localhost:8080/individual", value).done(function(data) {
        var outDiv = $("#out_div");
        outDiv.removeClass("invisible");
        outDiv.toggleClass("visible");
        $("#individual_output").val(data);
    });
}
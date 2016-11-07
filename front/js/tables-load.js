$(document).ready(function() {
    var table = $("#details-table");
    $.get("http://localhost:8080/db/get-details", function (data) {
        var i = 0;
        var bodyColor = $("body").css("background-color");
        document.tableRaw = data;
        for (var key in data) { 
            var row = $("<tr>");
            row.append($("<td>").text((i++)));
            row.append($("<td>").text(data[key].id));
            row.append($("<td>").text(new Date(data[key].creation_date).toDateString()));
            row.append($("<td>").text(data[key].warehouses_id));
            row.append($("<td>").text(data[key].factories_id));
            row.append($("<td>").text(data[key].cost));
            row.append($("<td>").text(data[key].designers_groups_id));
            var td = $("<td>")
            .attr("style", "background-color:"+bodyColor+";"+"border-top:0px!important;")
            var div = $("<div>")
            .attr("id","edit-row-controls")
            .attr("class","row")
            .attr("style","display:flex;visibility:visible;margin-left:5px;");
            var deleteButton = $("<div>").attr("class", "floating").append($("<i>")
                .attr("class","material-icons")
                .attr("id", "delete-row")
                .attr("style", "display:none;")
                .text("delete"));
            var editButton = $("<div>").attr("class", "floating").append($("<i>")
                .attr("class","material-icons")
                .attr("id", "edit-row")
                .attr("style", "display:none;")
                .text("edit"));
            var expandButton = $("<div>").attr("class", "floating").append($("<i>")
                .attr("class","material-icons")
                .attr("id", "expand-row")
                .attr("style", "display:none;")
                .text("details"));
            row.append(td.append(div.append(deleteButton).append(editButton).append(expandButton)));
            table.append(row);
        }
        $(".loader").remove();
        $("#delete-detail").click(function(){ 
            deleteRow($("#modal-head").text().substring(7));
        });
        $("#edit-detail").click(function() { 
            editRow($("#modal-head").text().substring(7));
        });
        $("#myModal").on('hidden.bs.modal', function () {
            $("#designers-table tbody tr").remove();
            $("#warehouse-output").val("");
            $("#factory-output").val("");
            $("#modal-head").text("");
            $("#wait").removeClass("invisible");
            $("#wait").addClass("visible");
            $("#detail_spec").addClass("invisible");
            $("#detail_spec").removeClass("visible");
        })
        $(".floating").mouseenter(function() {
            $(this).children().fadeIn(200);
        });
        $(".floating").mouseleave(function() {
            $(this).children().fadeOut(200);
        });
        $(".floating").click(function() {
            var tag = $(this).children()[0].id;
            var rawElement = document.tableRaw[$(this).parent().parent().parent().children().eq(0).html()];
            var id = rawElement.id;
            switch (tag) {
                case "expand-row":
                    expandRow(id);
                    break;
                case "delete-row":
                    deleteRow(id);
                    break;
                case "edit-row":
                    editRow(id);
                    break;
            }
        });
    });
});

function deleteRow(id) {
    $.get("http://localhost:8080/db/delete-detail/" + id, function (data) {
        if (data == "true") { 
            location.reload();
        }
    });
}

function editRow(id) {
    $.get("http://localhost:8080/db/get-detail/" + id, function (data) {
        var detail = data.detail;
        window.location.replace("http://localhost:8080/addOrUpdateDetail.html?cost="+detail.cost+
        "&warehouseId="+detail.warehouses_id+
        "&factoryId="+detail.factories_id+
        "&creationDate="+detail.creation_date+
        "&designersGroupId="+detail.designers_groups_id+
        "&detailId="+detail.id);
    });
}

function expandRow(id) {
    $("#myModal").modal();
    $("#modal-head").text("Detail " + id);
    $.getJSON("http://localhost:8080/db/get-detail/" + id, function(data) {
        $("#wait").removeClass("visible");
        $("#wait").addClass("invisible");
        $("#detail_spec").addClass("visible");
        $("#detail_spec").removeClass("invisible");
        $("#warehouse-output").val(data.warehouse);
        $("#factory-output").val(data.factory);
        var designersTable = $("#designers-table");
        var i = 0;
        for (var key in data.designers) { 
            designersTable.append("<tr>" 
            + "<td>" + (i++) + "</td>" 
            + "<td>" + data.designers[key].id + "</td>" 
            + "<td>" + data.designers[key].surname + "</td>"
            + "<td>" + data.designers[key].name + "</td>" 
            + "<td>" + data.designers[key].second_name + "</td>"
            + "</tr>");
        }
    });
}
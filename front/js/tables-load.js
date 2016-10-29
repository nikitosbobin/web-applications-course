$(document).ready(function() {
    var table = $("#details-table");
    $.get("http://localhost:8080/db/get-details", function (data) {
        var i = 0;
        for (var key in data) { 
            table.append("<tr>" 
            + "<td>" + (i++) + "</td>" 
            + "<td>" + data[key].id + "</td>" 
            + "<td>" + new Date(data[key].creation_date).toDateString() + "</td>"
            + "<td>" + data[key].warehouses_id + "</td>"
            + "<td>" + data[key].factories_id + "</td>" 
            + "<td>" + data[key].cost + " р" + "</td>"
            + "<td>" + data[key].designers_groups_id + "</td>"
            + "</tr>");
        }
        $(".loader").remove();
        $("#delete-detail").click(function(){ 
            $.get("http://localhost:8080/db/delete-detail/" + $("#modal-head").text().substring(7), function (data) {
                if (data == "true") { 
                    location.reload();
                }
            })
        });
        $("#edit-detail").click(function() { 
            $.get("http://localhost:8080/db/get-detail/" + $("#modal-head").text().substring(7), function (data) {
                var detail = data.detail;
                window.location.replace("http://localhost:8080/addOrUpdateDetail.html?cost="+detail.cost+
                "&warehouseId="+detail.warehouses_id+
                "&factoryId="+detail.factories_id+
                "&creationDate="+detail.creation_date+
                "&designersGroupId="+detail.designers_groups_id+
                "&detailId="+detail.id);
            });
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
        $("tr").click(function () {
            var id = $(this).children().eq(1).html();
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
        });
    });
});
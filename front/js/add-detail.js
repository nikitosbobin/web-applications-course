$(document).ready(function () {
    tryToFillDocument();
    $(".errorable").change(function () {
        $(this).parent().removeClass("has-warning");
    });
    $("#add-detail").click(function () {
        if (!checkInputsNotEmpty()) {
            return;
        }
        var date = $("#creation_date").val();
        date = new Date(date).getTime();
        var warehouse = $("#warehouses_id").val();
        var factory = $("#factory_id").val();
        var group = $("#group_id").val();
        var cost = $("#detail_cost").val();
        var id = $("#modal-head").text().substring(7);
        if (!id) {
            id = 0;
        }
        var resultDetail = "{"+
                                "\"creation_date\":\""+date+
                                "\",\"warehouses_id\":\""+warehouse+
                                "\",\"factories_id\":\""+factory+
                                "\",\"cost\":\""+cost+
                                "\",\"id\":\""+id+
                                "\",\"designers_groups_id\":\""+group+"\""
                            +"}";
        $.post("http://localhost:8080/db/addOrUpdate", resultDetail).done(function (data) {
            debugger;
            if (data == "true") {
                window.location.replace("http://localhost:8080/tables.html");
            } else if (data.indexOf("invalid") == 0) {
                debugger;
                $("#"+data.substring(8)).parent().addClass("has-warning");
            }
        });
    });
});

function tryToFillDocument() {
    var href = window.location.href;
    var startQueryIndex = href.indexOf("?");
    if (startQueryIndex === -1) {
        $("#creation_date").val(getFormattedDate(new Date()));    
        return;
    }
    var query = href.substring(startQueryIndex + 1);
    var map = parseQuery(query);
    var date = new Date(parseInt(map.creationDate));
    var warehouse = map.warehouseId;
    var factory = map.factoryId;
    var group = map.designersGroupId;
    var cost = map.cost;
    var detailId = map.detailId;
    $("#creation_date").val(getFormattedDate(date));
    $("#warehouses_id").val(warehouse);
    $("#factory_id").val(factory);
    $("#group_id").val(group);
    $("#detail_cost").val(cost);
    $("#modal-head").text("Detail " + detailId);
}

function parseQuery(query) {
    var result = {};
    var segments = query.split('&');
    for (var i = 0; i < segments.length; ++i) {
        var seg = segments[i].split("=");
        result[seg[0]] = seg[1];
    }
    return result;
}

function checkInputsNotEmpty() {
    var date = $("#creation_date")
    var wareHouse = $("#warehouses_id");
    var factory = $("#factory_id");
    var group = $("#group_id");
    var cost = $("#detail_cost");
    if (!date.val()) {
        date.parent().addClass("has-warning");
        return false;
    }
    if (!wareHouse.val()) {
        wareHouse.parent().addClass("has-warning");
        return false;
    }
    if (!group.val()) {
        group.parent().addClass("has-warning");
        return false;
    }
    if (!factory.val()) {
        factory.parent().addClass("has-warning");
        return false;
    }
    if (!cost.val()) {
        cost.parent().addClass("has-warning");
        return false;
    }
    return true;
}

function getFormattedDate(date) {
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getUTCDate();
    if (day < 10) {
        day = '0' + day;
    } 
    if (month < 10) {
        month = '0' + month;
    }
    return year + "-" + month + "-" + day;
}
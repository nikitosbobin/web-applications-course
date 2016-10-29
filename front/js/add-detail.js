$(document).ready(function () {
    tryToFillDocument();
});

function tryToFillDocument() {
    var href = window.location.href;
    var startQueryIndex = href.indexOf("?");
    if (startQueryIndex === -1) {
        return;
    }
    var query = href.substring(startQueryIndex + 1);
    var map = parseQuery(query);
    var date = new Date(parseInt(map.creationDate));
    var warehouse = map.warehouseId;
    var factory = map.factoryId;
    var group = map.designersGroupId;
    var cost = map.cost;
    console.log(getFormattedDate(date));
    $("#creation_date").val(getFormattedDate(date));
    $("#warehouses_id").val(warehouse);
    $("#factory_id").val(factory);
    $("#group_id").val(group);
    $("#detail_cost").val(cost);
}
//http://localhost:8080/addDetail.html?cost=100500&warehouseId=33&factoryId=18&creationdate=1401559200000&designersGroupId=3
function parseQuery(query) {
    var result = {};
    var segments = query.split('&');
    for (var i = 0; i < segments.length; ++i) {
        var seg = segments[i].split("=");
        result[seg[0]] = seg[1];
    }
    return result;
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
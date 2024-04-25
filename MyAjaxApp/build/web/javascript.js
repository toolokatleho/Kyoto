/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
var completeField;
var completeTable;
var autoRow;
var req;
var isIE;
function init(){
     completeField = document.getElementById("complete-field");
     completeTable = document.getElementById("complete-table");
     autoRow = document.getElementById("auto-row");
     completeTable.style.top = getElementY(autoRow) + "px";
}
function doCompletion(){
    var url = "autocomplete?action=complete&id="+encodeURIComponent(completeField.value);
    req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = callback;
    req.send(null);
}
function initRequest(){
    if(window.XMLHttpRequest){
       if(navigator.userAgent.indexOf('MSIE')!== -1){
           isIE = true;
       } 
       return new XMLHttpRequest();
    }else if(window.ActiveXObject){
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
        
    }
}
function callback(){
    
    clearTable();
    
    if(req.readyState === 4){
        if(req.status === 200){
            parseMessages(req.responseXML);
        }
    }
}
function appendComposer(firstName,lastName,composerId) {

    var row;
    var cell;
    var linkElement;

    if (isIE) {
        completeTable.style.display = 'block';
        row = completeTable.insertRow(completeTable.rows.length);
        cell = row.insertCell(0);
    } else {
        completeTable.style.display = 'table';
        row = document.createElement("tr");
        cell = document.createElement("td");
        row.appendChild(cell);
        completeTable.appendChild(row);
    }

    cell.className = "popupCell";

    linkElement = document.createElement("a");
    linkElement.className = "popupItem";
    linkElement.setAttribute("href", "autocomplete?action=lookup&id=" + composerId);
    linkElement.appendChild(document.createTextNode(firstName + " " + lastName));
    cell.appendChild(linkElement);
}
function getElementY(element){

    var targetTop = 0;

    if (element.offsetParent) {
        while (element.offsetParent) {
            targetTop += element.offsetTop;
            element = element.offsetParent;
        }
    } else if (element.y) {
        targetTop += element.y;
    }
    return targetTop;
}
function clearTable() {
    while(completeTable.firstChild){
        completeTable.removeChild(completeTable.firstChild);
    }
}
function parseMessages(responseXML) {

    // no matches returned
    if (responseXML === null) {
        return false;
    } else {

        var composers = responseXML.getElementsByTagName("composers");

        if (composers.length > 0) {
            
            for (var i = 0; i < composers.length; i++) {
                var composer = composers[i];
                var firstName = composer.getElementsByTagName("firstName")[0].textContent;
                var lastName = composer.getElementsByTagName("lastName")[0].textContent;
                var composerId = composer.getElementsByTagName("id")[0].textContent;
                appendComposer(firstName,lastName,composerId);
            }
        }
    }
}
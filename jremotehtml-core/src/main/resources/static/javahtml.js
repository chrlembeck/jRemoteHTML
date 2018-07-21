function sendMessage(data) {
   var request = new XMLHttpRequest();
    request.open("POST", "javahtml");
    request.addEventListener('load', function(event) {
        if (request.status >= 200 && request.status < 300) {
            console.log(request.responseText);
            var json = JSON.parse(request.responseText);
        	console.log(json);
        	for (var i = 0; i < json.length; i++) {
        	    console.log(json[i]);
        	    var action = json[i].action;
        	    if (action === "appendTag") {
        	    	appendTag(json[i].parentId, json[i].content);
        	    } else if (action === "newClickListener") {
        	    	newClickListener(json[i].elementId);
        	    }
        	    
        	}
        	
        } else {
            console.warn(request.statusText, request.responseText);
        }
    });
    var message = JSON.stringify(data);
    console.log("sending message: " + message);
    request.send(message);
}

function loadContent() {
    var data = {"action":"loadContent"};
    console.log("load content " + data);
    sendMessage(data);
}

// Erstellt einen neuen child Knoten an dem Knoten mit der übergebenen parentId
// und füllt den Knoten mit dem als content übergebenen HTML-Fragment
function appendTag(parentId, content) {
    var newNode = document.createDocumentFragment();
    var temp = document.createElement('div');
    temp.innerHTML = content;
    while (temp.firstChild) {
        newNode.appendChild(temp.firstChild);
    }
    var parent = document.getElementById(parentId);
    parent.appendChild(newNode);
}

function newClickListener(elementId) {
    var element = document.getElementById(elementId);
    element.addEventListener("click", function() {
        console.log("clicked: " + elementId);
        var data = {"action": "elementClicked", "elementId":elementId};
        sendMessage(data);    
    });
}

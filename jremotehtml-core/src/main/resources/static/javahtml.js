function sendMessage(data) {
   var request = new XMLHttpRequest();
    request.open("POST", "javahtml");
    request.addEventListener('load', function(event) {
        if (request.status >= 200 && request.status < 300) {
            console.log(request.responseText);
            var json = JSON.parse(request.responseText);
        	console.log(json);
        	for (var i = 0; i < json.length; i++) {
        		var currentChange = json[i];
        	    console.log(currentChange);
        	    var action = currentChange.action;
        	    if (action === "insertTag") {
        	    	insertTag(currentChange.parentId, currentChange.content, currentChange.position);
        	    } else if (action === "removeElement") {
        	    	removeElement(currentChange.parentId, currentChange.position, currentChange.childId);
        	    } else if (action === "newClickListener") {
        	    	newClickListener(currentChange.elementId);
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

// Erstellt einen neuen child-Knoten an dem Knoten mit der übergebenen parentId
// und füllt den Knoten mit dem als content übergebenen HTML-Fragment
function insertTag(parentId, content, position) {
	console.log("inserting node on " + parentId + " at position " + position);
    var parent = document.getElementById(parentId);
    var newNode = document.createDocumentFragment();
    var temp = document.createElement(parent.nodeName);
    temp.innerHTML = content;
    while (temp.firstChild) {
        newNode.appendChild(temp.firstChild);
    }
    var children = parent.childNodes;
    if (position >= children.length) {
        parent.appendChild(newNode);
    } else {
    	parent.insertBefore(newNode, children[position]);
    }
}

function removeElement(parentId, position, childId) {
	console.log("removing element from element " + parentId + " with position " + position + " and id " + childId);
	var parent = document.getElementById(parentId);
	if (childId) {
		var child = document.getElementById(childId);
		parent.removeChild(child);
	} else {
		var child = parent.childNodes[position];
		parent.removeChild(child);
	}
}

function newClickListener(elementId) {
    var element = document.getElementById(elementId);
    element.addEventListener("click", function() {
        console.log("clicked: " + elementId);
        var data = {"action": "elementClicked", "elementId":elementId};
        sendMessage(data);    
    });
}

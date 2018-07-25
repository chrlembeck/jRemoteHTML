function JRemoteHTML() {

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
	        	    } else if (action === "attributeModified") {
	        	    	attributeModified(currentChange.elementId, currentChange.key, currentChange.value);
	        	    } else if (action === "attributeRemoved") {
	        	    	attributeRemoved(currentChange.elementId, currentChange.key);
	        	    } else if (action === "styleModified") {
	        	    	styleModified(currentChange.elementId, currentChange.key, currentChange.value);
	        	    } else if (action === "styleRemoved") {
	        	    	styleRemoved(currentChange.elementId, currentChange.key);
	        	    } else if (action === "textModified") {
	        	    	textModified(currentChange.parentId, currentChange.position, currentChange.text);
	        	    } else if (action === "modifyClickListener") {
	        	    	modifyClickListener(currentChange.elementId, currentChange.enabled);
	        	    } else {
	        	    	alert("unknown action '" + action + "'.");
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
	
	function splitTextFields(text) {
		var result = [];
		var part = "";
		var escaping = false;
		for (var i = 0; i < text.length; i++) {
			var current = text.charAt(i);
			if (escaping) {
				part += current;
				escaping = false;
			} else {
	    		if (current == "\\") {
					escaping = true;
	    		} else {
					if (current == "|") {
						result.push(part);
						part = "";
					} else {
	    				part += current;
					}
				}
			}
		}
		result.push(part);
		return result;
	}
	
	function splitTextNodes(node) {
		if (node.nodeType == 3) { 
			// text node
			var text = node.nodeValue;
			var parts = splitTextFields(text);
			if (parts.length > 1) {
			    console.log("splitted " + text + " into " + parts);
			    node.nodeValue = parts[parts.length - 1];
			    for (var i = parts.length-2; i >= 0; i--) {
			    	var newNode = document.createTextNode(parts[i]);
			    	node.parentNode.insertBefore(newNode, node);
			    }
			}
		} else { 
			// recursion
			var children = node.childNodes;
			for (var i = 0; i < children.length; i++) {
				splitTextNodes(children[i]);
			}
		}
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
	    splitTextNodes(newNode);
	    
	    
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
	
	function attributeModified(elementId, key, value) {
		console.log("attribute " + key + " modified on element " + elementId + " to " + value);
		var element = document.getElementById(elementId);
		element.setAttribute(key, value === undefined?'':value);
	}
	
	function attributeRemoved(elementId, key) {
		console.log("remove attribute " + key + " from element " + elementId);
		var element = document.getElementById(elementId);
		element.removeAttribute(key);
	}
	
	function styleModified(elementId, key, value) {
		console.log("style " + key + " modified on element " + elementId + " to " + value);
		var element = document.getElementById(elementId);
		element.style.setProperty(key, value === undefined?'':value);
	}
	
	function styleRemoved(elementId, key) {
		console.log("remove style " + key + " from element " + elementId);
		var element = document.getElementById(elementId);
		element.style.removeProperty(key);
	}
	
	function textModified(parentId, position, text) {
		console.log("modify text on node " + parentId + " at Position " + position + " to " + text);
		var parent = document.getElementById(parentId);
		parent.childNodes[position].nodeValue = text;
	}
	
	var clickListenerAction = function(event) {
		var elementId = event.target.id;
	    console.log("clicked: " + elementId);
	    var data = {"action":"elementClicked", "elementId":elementId};
	    sendMessage(data);    
	};
	
	function modifyClickListener(elementId, enabled) {
	    var element = document.getElementById(elementId);
	    
	    if (enabled) {
	        element.addEventListener("click", clickListenerAction);
	    } else {
	    	element.removeEventListener("click", clickListenerAction);
	    }
	}
	
	function registerComponent(component) {
	    console.log("registering component " + component);
	    component.initialize();
	}
	
	return {
	    // öffentliche definitionen
	  	loadContent: loadContent,
	  	registerComponent: registerComponent
	};

}

var jremotehtml = new JRemoteHTML();
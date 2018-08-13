function JRemoteHTMLComponents() {

    var dataTableClickListener = function(event) {
		console.log(event);
		if (event.target.tagName == "TR" || event.target.tagName == "TD") {
			var row = event.target;
			while (row.tagName != "TR") {
				row = row.parentElement;
			}
			selectDataTableRow(row);
		}
	};

	function selectDataTableRow(row) {
		var tbody = row.parentElement;
		var rows = tbody.querySelectorAll("tr.jremotehtml-datatable-row");
		rows.forEach(function(currentValue, currentIndex, listObj) {
			currentValue.removeAttribute("selected");
		});

		row.setAttribute("selected", "selected");
	}

	var dataTableKeyListener = function(event) {
		console.log(event);
		if (event.target.tagName == "TR") {
			var row = event.target;
			if (event.keyCode == 32) {
				// Space
				selectDataTableRow(row);
			} else if (event.keyCode == 40) {
				// ArrowDown
				var nextRow = row.nextSibling;
				while (nextRow && nextRow.tagName != "TR") {
					nextRow = nextRow.nextSibling;
				}
				if (nextRow) {
					nextRow.focus();
				}
			} else if (event.keyCode == 38) {
				// ArrowUp
				var prevRow = row.previousSibling;
				while (prevRow && prevRow.tagName != "TR") {
					prevRow = prevRow.previousSibling;
				}
				if (prevRow) {
					prevRow.focus();
				}
			}
		}
	};

	function initializeDataTable(element) {
	    console.log("initializing DataTable");
		var elements = element.querySelectorAll("tr.jremotehtml-datatable-row");
		for (var i = 0; i < elements.length; i++) {
			var row = elements[i];
			row.addEventListener("click", dataTableClickListener);
			row.addEventListener("keydown", dataTableKeyListener);
		}
	}

    function initialize() {
        console.log("initializing JRemoteHTMLComponents");
    }
    
    function initComponent(componentId, element) {
        if (componentId === "jremotehtml-datatable") {
            initializeDataTable(element);
        }
    }

    return {
        initialize: initialize,
        initComponent: initComponent
    };
}

jremotehtml.registerComponent(new JRemoteHTMLComponents());
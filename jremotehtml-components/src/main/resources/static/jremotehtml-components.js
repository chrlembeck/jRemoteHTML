function JRemoteHTMLComponents() {

    function initialize() {
        console.log("initializing JRemoteHTMLComponents");
    }

    return {
        initialize: initialize;
    };
}

var component = new JRemoteHTMLComponents();

jremotehtml.registerComponent(component);
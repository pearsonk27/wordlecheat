const React = require('react'); 
const ReactDOM = require('react-dom'); 
const client = require('./client'); 

class App extends React.Component {

	render() {
		return (
			<h2>App JS Return</h2>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
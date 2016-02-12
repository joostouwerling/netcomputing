<!DOCTYPE html>
<html>
<body>
<h2>Hello World!</h2>
<p>Go to <a href="rest/hello">rest/hello</a> for the REST endpoint</p>

<pre id="response"></pre>
<script>
	// small example of using the endpoint to request a XML response.
	// (More often you might end up requesting JSON, which is easier to work with in JS.)
	var request = new XMLHttpRequest();
	request.open('GET', 'rest/hello', true);
	request.setRequestHeader('Accept', 'text/xml');
	request.onreadystatechange = function() {
		if (request.readyState === 4) {
			try {
				document.getElementById('response').textContent = 'Response: ' + request.responseXML.getElementsByTagName('hello').item(0).textContent;
			} catch (e) {
				document.getElementById('response').textContent = 'Error: ' + e;
			}
		}
	}
	request.send();
</script>
</body>
</html>

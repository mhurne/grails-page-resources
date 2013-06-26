<html>
    <head>
        <meta name="layout" content="main">
    </head>
    <body>
        <g:form name="greeting-form">
            <label>Name: <g:textField name="name"/></label>
            <g:submitToRemote action="greet" onSuccess="handleGreetingResponse(data, textStatus)" value="Greet"/>
        </g:form>
        <ul id="greeting-log"></ul>
    </body>
</html>

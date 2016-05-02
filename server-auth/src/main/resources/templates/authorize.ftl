<html>
<head>
    <link rel="stylesheet" href="../../css/app.css"/>
</head>
<body>
<div class="container">
    <h2>Please Confirm</h2>

    <p>
        Do you authorize "${authorizationRequest.clientId}" at "${authorizationRequest.redirectUri}" to access your protected resources
        with scope ${authorizationRequest.scope?join(", ")}.
    </p>
    <form id="confirmationForm" name="confirmationForm"
          action="auth/oauth/authorize" method="post">
        <input name="user_oauth_approval" value="true" type="hidden"/>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Approve</button>
    </form>

    <button class="btn btn-primary" type="button" onclick="location.href = '/'">Deny</button>
</div>
</body>
</html>
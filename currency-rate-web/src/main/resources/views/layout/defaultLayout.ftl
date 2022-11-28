<#macro defaultLayout>
    <!DOCTYPE html>
    <html lang="${lang!}">
    <head>
        <title>Currency Rate</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="chrome=1,IE=edge"/>
        <meta http-equiv="cleartype" content="on">
        <meta name="robots" content="noindex, nofollow"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
        <meta name="google" content="notranslate"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css" crossorigin="anonymous"
              referrerpolicy="no-referrer">
    </head>
    <body>
    <div>
        <div class="container">
            <#nested/>
        </div>
    </div>
    </body>
    </html>
</#macro>

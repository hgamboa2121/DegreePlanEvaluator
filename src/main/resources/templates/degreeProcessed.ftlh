<!DOCTYPE html>
<html>
    <head>
        <title>Degree Proccessed</title>
        <meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link rel="stylesheet" href="assets/css/main.css" />
    </head>
    <body>
	<body class="is-preload">
		<!-- Header -->
		<header id="header">
			<a class="logo" href="index.html">The University of Texas at El Paso</a>
		</header>
			<div id="heading" >
					<h1> Computer Science Degree Plan Evaluator</h1>
			</div>
            <section class="wrapper">
			<div class="inner">
                <h2 style="color:black;">Courses Needed</h2>
                    <ol>
                        <#list needed as need>
                        <li style="color:#FF6F05;">${need}</li>
                        </#list>
                    </ol>
                 <h2 style="color:black;">Courses that need a better grade</h2>
                    <ol>
                        <#list betterGrades as grade>
                        <li style="color:red;">${grade}</li>
                        </#list>
                     </ol>
                <h2 style="color:black;">Courses that need attention</h2>
                     <ol>
                        <#list attention as att>
                        <li style="color:blue;">${att}</li>
                        </#list>
                    </ol>
                <h2 style="color:black;">Courses Passed</h2>
                    <ol>
                        <#list passed as pass>
                        <li style="color:#608D4F;">${pass}</li>
                         </#list>
                     </ol>
            </div>
		    </section>      
    </body>
</html>
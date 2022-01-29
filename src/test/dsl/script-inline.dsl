workspace {

    !script groovy {
        println("Hello from Groovy");
        workspace.model.addPerson("Groovy");
    }

    !script kotlin {
        println("Hello from Kotlin");
        workspace.model.addPerson("Kotlin");
    }

    !script javascript {
        print("Hello from JavaScript");
        workspace.model.addPerson("JavaScript");
    }

    !script ruby {
        puts "Hello from Ruby"
        workspace.model.addPerson("Ruby");
    }

}
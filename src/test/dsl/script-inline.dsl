workspace {

    !script groovy {
        println("Hello from Groovy");
        workspace.model.addPerson("Groovy");
    }

    !script kotlin {
        println("Hello from Kotlin");
        workspace.model.addPerson("Kotlin");
    }

    !script ruby {
        puts "Hello from Ruby"
        workspace.model.addPerson("Ruby");
    }

}
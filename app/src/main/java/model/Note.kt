package model

data class Note(var topic: String? = null, var content: String? = null){
    var id: Int = -1
    constructor(id: Int, topic: String, content: String) : this(topic, content){
        this.id = id
    }
}

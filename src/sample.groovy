set 'port', System.getProperty("ratpack.port")

get("/") {
	def ua = headers['user-agent']
    "Your user-agent: ${ua}"
}
    
get("/foo/:name") {
	"Hello, ${urlparams.name}"
}
    
get("/person/:id") {
	"Person #${urlparams.id}"
}
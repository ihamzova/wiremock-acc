import com.jayway.jsonpath.JsonPath

body = context.get("request.body")
return JsonPath.read(body, "\$.test");
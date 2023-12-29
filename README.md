This is my first project, that involves API calls. 

Currently trying to get back to Java, so I decided to use it.
In the future I am planning on writing more description on here.

[Helpful info] :
Weather.java - method getJSONValue is recursively able to go through the json, if there are more layers to it. Method's input 'key' is split by the first character ':' into 2, and then the method steps into the json part before ':' character and checks again, if there are any ':' characters again until it is at the bottom layer (eg. input key is "parent:child:name", the method will get to name of specified child of a specified parent)

[Note] :
Input query is not tested in any way, in this case, it should only lead to API call fail.

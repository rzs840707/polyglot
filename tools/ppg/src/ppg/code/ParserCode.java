package ppg.code;

public class ParserCode extends Code
{
    String classname;
    String extendsimpls;
    public ParserCode (String classname, String extendsimpls,
		       String parserCode)
    {
	this.classname = classname;
	this.extendsimpls = extendsimpls;
	value = parserCode;
    }
    @Override
    public Object clone ()
    {
	return new ParserCode (classname, extendsimpls, value);
    }
    @Override
    public String toString ()
    {
	if (classname == null)
	    classname = "code";
	return "parser " + classname + extendsimpls +
	    " {:\n" + value + "\n:}\n";
    }
}

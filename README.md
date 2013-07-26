<h1>Gradebook Example</h1>

<p>This project is a quick example of how to do OAuth authentication
to BLTI</p>

<p>The three most important things to remember are:</p>
<ol>
  <li>Unlike normal oauth, for lti you do <em><b>NOT</b></em> include
  the oauth_token in anything</li>
  <li>Unlike normal oauth, you do <em><b>NOT</b></em> append the token
  secret to the key used to HMAC-SHA1 hash the signature.</li>
  <li>The oauth_timestamp <em><b>MUST</b></em> be shorter than 32 bits</li>
</ol>

<p>This is a maven project. It should build by typing:</p>
<pre>
mvn clean compile
</pre>

<p>There are no unit tests. I fixed this on behalf of someone else and
the powers that be did not allocate time for unit testing. If someone
wanted to write unit tests... <em>Hint Hint</em></p>


<p>You will also need to import the google oauth jars since they don't
appear to be in maven central</p>
<pre>
mvn install:install-file -DgroupId=net.oauth.core -DartifactId=oauth -Dversion=20090530 -Dpackaging=jar -Dfile=./lib/oauth-20090530.jar
mvn install:install-file -DgroupId=net.oauth.core -DartifactId=oauth-provider -Dversion=20090530 -Dpackaging=jar -Dfile=./lib/oauth-provider-20090530.jar
mvn install:install-file -DgroupId=oauth.signpost -DartifactId=oauth-signpost -Dversion=1.2.1.2 -Dpackaging=jar -Dfile=./lib/signpost-core-1.2.1.2.jar
</pre>


<p>To run this code and prove it works:</p>
<pre>
mvn clean compile exec:java -Dimsx.message.identifier="" -Dimsx.sourceId="" -Doauth.consumer.key="" -Doauth.shared.secret=""
</pre>

<p>There are other properties to mess with in the pom.</p>
<p>I hope this helps</p>

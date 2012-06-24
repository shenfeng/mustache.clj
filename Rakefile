desc "Run unit test"
task :test do
  sh 'rm classes -rf && lein javac && lein test'
end

desc "Install in local repository"
task :install_local => :test do
  sh 'lein deps && rm *.jar pom.xml classes -rf && lein jar && lein install'
  sh 'cd ~/workspace/rssminer && lein deps'
end


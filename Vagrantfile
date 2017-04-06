# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "ubuntu/trusty64"

  # expose port 8080
  # config.vm.network :forwarded_port,  guest: 8080,    host: 8080
  
  # expose port 5432 for PostgreSQL
  # config.vm.network :forwarded_port,  guest: 5432,    host: 5432

  # expose port 3306 for MySQL
  config.vm.network :forwarded_port,  guest: 3306,    host: 3306

  # run the install script for dependencies
  config.vm.provision :shell, :path => "bootstrap.sh"
  
  # increase memory
  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
  #  v.cpus = 2
  end
  
end
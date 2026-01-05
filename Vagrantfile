Vagrant.configure("2") do |config|

  # Base OS
  config.vm.box = "ubuntu/jammy64"

  # Sync current project folder to /vagrant inside VM
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"

  # VM resources
  config.vm.provider "virtualbox" do |vb|
    vb.memory = 4096  # Change this from 2048 to 4096
    vb.cpus = 2
  end

  # Run Ansible inside the VM
  config.vm.provision "ansible_local" do |ansible|
    ansible.playbook = "playbooks/setup.yml"
  end

end

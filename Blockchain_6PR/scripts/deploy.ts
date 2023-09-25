const hre = require('hardhat');
const ethers = hre.ethers;
const fs = require('fs');
const path = require('path');

async function main(){
  const [deployer] = await ethers.getSigners();

  console.log("Deploying with", await deployer.getAddress());

  const Payments = await ethers.getContractFactory("Payments", deployer);
  const paymentDeploy = await Payments.deploy();

  console.log("Payments smart-contract deployed in - " + paymentDeploy.getAddress());

  await paymentDeploy.deployed();
}

main();

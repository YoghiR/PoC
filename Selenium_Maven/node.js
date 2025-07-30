// fetch-latest-angular-version
const { exec } = require('child_process');

const packageName = 'core-js'; // or any other Angular package

exec(`npm show ${packageName} version`, (err, stdout, stderr) => {
    if (err) {
        console.error(`Error fetching version: ${stderr}`);
        return;
    }
    console.log(`${packageName}: ${stdout.trim()}`);
});

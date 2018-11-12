const {AutoLanguageClient} = require('atom-languageclient');
const cp = require('child_process');
const os = require("os");
const path = require("path");
class MyDslLanguageClient extends AutoLanguageClient {
    getGrammarScopes() {
        return ['source.tptp']
    }
 
    getLanguageName() {
        return 'Tptp (Xtext)'
    }
 
    getServerName() {
        return 'Tptp (Xtext) Language Server'
    }
 
    startServerProcess(projectPath) {
        const serverHome = path.join(__dirname, 'tptp');
        const args = [];
        let launcher = os.platform() === 'win32' ? 'tptp-standalone.bat' : 'tptp-standalone';
        let script = path.join(__dirname, 'tptp', 'bin', launcher)
        const childProcess = cp.spawn(script, args,{ cwd: serverHome });
        this.captureServerErrors(childProcess);
        childProcess.on('close', exitCode => {
            if (!childProcess.killed) {
                atom.notifications.addError('IDE-MyDsl language server stopped unexpectedly.', {
                    dismissable: true,
                    description: this.processStdErr ? `<code>${this.processStdErr}</code>` : `Exit code ${exitCode}`
                })
            }
        });
        return childProcess;
    }
}
 
module.exports = new MyDslLanguageClient();

pipeline {
    agent any

    tools {
        maven 'Maven' // Nom de l'installation Maven dans Jenkins
    }

    environment {
        SONARQUBE = 'SonarQube-10' // Nom du serveur Sonar configuré dans Jenkins > Manage Jenkins > Configure System
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/hasnahatti70/sonar-java17-example.git'
            }
        }

        stage('Gitleaks Scan') {
            steps {
                bat '''
                echo 🔍 Lancement de Gitleaks...
                "C:\\Users\\MTechno\\Downloads\\gitleaks_8.26.0_windows_x64\\gitleaks.exe" detect --source=. --verbose --report-format=json --report-path=gitleaks-report.json || exit /b 0

                echo 📄 Résultat du scan :
                type gitleaks-report.json || echo ⚠️ Aucun secret trouvé.
                '''
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    bat 'mvn sonar:sonar -Dsonar.projectKey=sonar-java17-example -Dsonar.coverage.jacoco.xmlReportPaths=target/test-results/coverage/jacoco/jacoco.xml'
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline exécutée avec succès 🎉"
        }
        failure {
            echo "❌ Échec du pipeline. Vérifiez les logs."
        }
        always {
            archiveArtifacts artifacts: 'gitleaks-report.json', allowEmptyArchive: true
        }
    }
}

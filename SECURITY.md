# Security Policy

ObservaStack is a public portfolio case by JRS Software. It is not intended to store personal, sensitive or production data.

## Supported Versions

| Version | Supported |
| --- | --- |
| 0.1.x | Yes |

## Reporting a Vulnerability

Open a private advisory in the repository or contact the maintainers through the official JRS Software channels.

## Security Practices Demonstrated

- Dependency automation with Dependabot.
- Filesystem and Docker image scanning with Trivy.
- Non-root container runtime user.
- Explicit actuator endpoint exposure.
- No hardcoded secrets.
- `.env.example` for local configuration.

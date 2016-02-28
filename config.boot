firewall {
    all-ping enable
    broadcast-ping disable
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians enable
    name WAN_IN {
        default-action drop
        description "Packets from Internet to LAN"
        enable-default-log
        rule 1 {
            action accept
            description "allow established sessions"
            log disable
            protocol all
            state {
                established enable
                invalid disable
                new disable
                related enable
            }
        }
        rule 2 {
            action drop
            description "drop invalid state"
            log disable
            protocol all
            state {
                established disable
                invalid enable
                new disable
                related disable
            }
        }
    }
    name WAN_LOCAL {
        default-action drop
        description "Packets from Internet to the router"
        rule 1 {
            action accept
            description "allow established session to the router"
            log disable
            protocol all
            state {
                established enable
                invalid disable
                new disable
                related enable
            }
        }
        rule 2 {
            action drop
            description "drop invalid state"
            log disable
            protocol all
            state {
                established disable
                invalid enable
                new disable
                related disable
            }
        }
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
}
interfaces {
    bridge br0 {
        aging 300
        bridged-conntrack disable
        hello-time 2
        max-age 20
        priority 32768
        promiscuous disable
        stp false
    }
    ethernet eth0 {
        address 192.168.1.1/24
        description LAN1
        duplex auto
        speed auto
    }
    ethernet eth1 {
        description ONT
        duplex auto
        speed auto
        vif 832 {
            address dhcp
            description "Internet Orange DHCP"
            dhcp-options {
                client-option "send dhcp-client-identifier xxxx;"
                client-option "send vendor-class-identifier &quot;sagem&quot;;"
                client-option "send user-class &quot;+FSVDSL_livebox.Internet.softathome.Livebox3&quot;;"
                client-option "send rfc3118-authentication xxxx;"
                client-option "request subnet-mask, routers, domain-name-servers, domain-name, broadcast-address, dhcp-lease-time, dhcp-renewal-time, dhcp-rebinding-time, option-119, option-90, option-120;"
                default-route update
                default-route-distance 210
                name-server update
            }
            firewall {
                in {
                    name WAN_IN
                }
                local {
                    name WAN_LOCAL
                }
            }
            traffic-policy {
            }
        }
        vif 838 {
            bridge-group {
                bridge br0
            }
            description TV
        }
        vif 840 {
            bridge-group {
                bridge br0
            }
            description TV
        }
    }
    ethernet eth2 {
        description LAN2
        duplex auto
        speed auto
        vif 832 {
            address 192.168.2.1/24
        }
        vif 838 {
            bridge-group {
                bridge br0
            }
            description TV
        }
        vif 840 {
            bridge-group {
                bridge br0
            }
            description TV
        }
    }
    loopback lo {
    }
}
service {
    dhcp-server {
        disabled false
        global-parameters "option SIP code 120 = string;"
        global-parameters "option authsend code 90 = string;"
        hostfile-update disable
        shared-network-name LAN1 {
            authoritative disable
            subnet 192.168.1.0/24 {
                default-router 192.168.1.1
                dns-server 192.168.1.1
                lease 86400
                start 192.168.1.2 {
                    stop 192.168.1.200
                }
            }
        }
        shared-network-name LAN2 {
            authoritative disable
            subnet 192.168.2.0/24 {
                default-router 192.168.2.1
                dns-server 81.253.149.1
                lease 86400
                start 192.168.2.2 {
                    stop 192.168.2.200
                }
                subnet-parameters "option authsend 00:00:00:00:00:00:00:00:00:00:00:64:68:63:70:6c:69:76:65:62:6f:78:66:72:32:35:30;"
                subnet-parameters "option SIP 00:06:73:62:63:74:33:67:03:41:55:42:06:61:63:63:65:73:73:11:6f:72:61:6e:67:65:2d:6d:75:6c:74:69:6d:65:64:69:61:03:6e:65:74:00;"
            }
        }
    }
    dns {
        forwarding {
            cache-size 150
            listen-on eth0
            listen-on eth2
        }
    }
    gui {
        https-port 443
    }
    nat {
        rule 5000 {
            description "Masquerading outgoing connections"
            log disable
            outbound-interface eth1.832
            protocol all
            type masquerade
        }
    }
    ssh {
        port 22
        protocol-version v2
    }
}
system {
    host-name ubnt
    login {
        user ubnt {
            authentication {
                encrypted-password xxxx
            }
            level admin
        }
    }
    ntp {
        server 0.ubnt.pool.ntp.org {
        }
        server 1.ubnt.pool.ntp.org {
        }
        server 2.ubnt.pool.ntp.org {
        }
        server 3.ubnt.pool.ntp.org {
        }
    }
    offload {
        ipv4 {
            forwarding enable
            vlan enable
        }
    }
    package {
        repository wheezy {
            components "main contrib non-free"
            distribution wheezy
            password ""
            url http://http.us.debian.org/debian
            username ""
        }
        repository wheezy-security {
            components main
            distribution wheezy/updates
            password ""
            url http://security.debian.org
            username ""
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:system@4:ubnt-pptp@1:ubnt-util@1:vrrp@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v1.8.0.4853089.160219.1607 */

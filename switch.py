"""Support for HLK-SW16 (old) switches."""
from homeassistant.components.switch import ToggleEntity

from . import DATA_DEVICE_REGISTER, SW16OldDevice
from .const import DOMAIN

PARALLEL_UPDATES = 0


def devices_from_entities(hass, entry):
    """Parse configuration and add HLK-SW16 (old) switch devices."""
    device_client = hass.data[DOMAIN][entry.entry_id][DATA_DEVICE_REGISTER]
    devices = []
    for i in range(16):
        device_relay = f"{i:01x}"
        device = SW16OldSwitch(device_relay, entry.entry_id, device_client)
        devices.append(device)
    return devices


async def async_setup_entry(hass, entry, async_add_entities):
    """Set up the HLK-SW16 platform."""
    async_add_entities(devices_from_entities(hass, entry))


class SW16OldSwitch(SW16OldDevice, ToggleEntity):
    """Representation of a HLK-SW16 (old) switch."""

    @property
    def is_on(self):
        """Return true if device is on."""
        return self._is_on

    async def async_turn_on(self, **kwargs):
        """Turn the device on."""
        await self._client.turn_on(self._device_relay)

    async def async_turn_off(self, **kwargs):
        """Turn the device off."""
        await self._client.turn_off(self._device_relay)
